package cn.gd.snm.testui2.svgtest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.PathParser
import cn.gd.snm.testui2.R
import org.w3c.dom.Element
import java.util.ArrayList
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.math.max
import kotlin.math.min

/**
 * Svg自定义view。
 *
 *  需求：
 *      1. 加载出svg图，要求宽高比不变，宽度占满屏幕宽度。
 *      2. 点击svg图，返回对应的path所在的index。
 *
 */
class TestSvgView :View{

    constructor(context: Context) : super(context){
        init()
    }
    constructor(context: Context, attributeSet: AttributeSet)
            :super(context,attributeSet){
        init()
    }
    var paint = Paint()
    /**
     * 解析svg，存储里面的path。
     *
     */
    private fun init() {
        paint.isAntiAlias = true

        thread.start()
    }

    //todo 目的是存储解析svg后的数据。
    val pathList: MutableList<ProviceItem> = ArrayList<ProviceItem>()
    var mapRect = RectF()
    /**
     * 目的是解析出svg中的path。
     *  1. 将所有path存储起来以便后面绘制。
     *  2. 找出svg中的上下左右四个点，确定布局大小。
     */
    private var thread = Thread{
        //todo svg实际就是个xml文件，所以需要读取并解析xml所有节点。
        val inputStream = context.resources.openRawResource(R.raw.china)
        val factory = DocumentBuilderFactory.newInstance()
        val builder: DocumentBuilder = factory.newDocumentBuilder()
        val parse = builder.parse(inputStream)
        val documentElement = parse.documentElement
        val items = documentElement.getElementsByTagName("path")

        // todo 定义整个地图的左上角和右下角的坐标点，接下来要循环去遍历每个Path，
        //  找出边界值并赋值。
        var left = -1f
        var bottom = -1f
        var top = -1f
        var right = -1f

        //循环获取每个节点
        for(i in 0 until items.length){

            //todo 解析并存储path，这里我们只解析绘制字段pathData
            var element = items.item(i) as Element
            var pathData = element.getAttribute("android:pathData")
            //读取数据后，还要调用对应的解析，此时返回的才是真正的Path对象。
            val path = PathParser.createPathFromPathData(pathData)

            //存储解析后的path
            var pathItem = ProviceItem(path)
            pathList.add(pathItem)

            //todo 接下来确定整个地图的布局，用一个大矩形装起来。
            //先确定每个Path省份所在的矩形，目的是确定边界
            var rect = RectF()
            path.computeBounds(rect, true)

            //todo 找出边界。确定当前Path省份所在矩形后，接下来就可以获取到矩形的四个角坐标。
            left = if (left == -1f) rect.left else min(left, rect.left)
            right = if (right == -1f) rect.right else max(right, rect.right)
            top = if (top == -1f) rect.top else min(top, rect.top)
            bottom = if (bottom == -1f) rect.bottom else max(bottom, rect.bottom)

        }

        // todo 存储整个地图的边界
        mapRect.left = left
        mapRect.bottom = bottom
        mapRect.top = top
        mapRect.right = right

        // todo 接下来可以去刷新Ui了
        uiHandler.sendEmptyMessage(0)
    }

    //随意初始化四个颜色
    private val colorArray = longArrayOf(0xFF239BD7, 0xFF30A9E5,0xFF80CBF1,
        0xFFFFFFFF)
    //是否解析svg完成
    var isParsePathEnd = false
    /**
     * Ui线程
     *
     */
    private val uiHandler = object:Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            if(pathList.size <= 0){
                return
            }

            for(index in pathList.indices){
                var color = Color.WHITE
                val flag = (index % 4)
                color = when (flag) {
                    1 -> colorArray[0].toInt()
                    2 -> colorArray[1].toInt()
                    3 -> colorArray[2].toInt()
                    else -> Color.CYAN
                }

                pathList[index].color = color
                isParsePathEnd = true

                //todo 重新走测量,绘制
                measure(measuredWidth, measuredHeight)
                postInvalidate()
            }
        }
    }

    /**
     * 判断点击在对应的哪个path区域
     *
     *  如果是-1，则点击不在区域内，否则打印区域所在的index。
     *
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(isParsePathEnd){
            var pathIndex=-1

            for(index in pathList.indices){
                //todo 这里注意要除下绘制时的缩放，因为本身map对应的path并没有布满屏幕宽度，也就是说
                    // path本身的坐标并没有变化，只不过绘制的时候缩放了。
                if(pathList[index].isContain(event!!.x/mapScale,event.y/mapScale)){
                    pathIndex = index
                    Log.d("darrenTest","index=${pathIndex}")
                }
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 绘制path。
     *
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

//        setBackgroundColor(context.resources.getColor(R.color.purple_200))

        if(!isParsePathEnd || pathList.size <=0){
            return
        }

        //todo 目的是让地图填满屏幕的宽度，且宽高比不变
        canvas!!.scale(mapScale,mapScale)

        //todo 开始循环绘制path，把绘制path的方式封装进item本身。
        for(item in pathList){
            item.drawPath(canvas,paint)
        }
    }

    //获取map占满屏幕宽所需比例
    private var mapScale=-1f
    //避免重复测量
    private var isOnMeasureLoaded = false
    /**
     * 确定当前view的大小。
     *
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //todo 如果svg还没加载完毕。
        if(!isParsePathEnd || isOnMeasureLoaded){
            return
        }

        isOnMeasureLoaded = true
        var width = MeasureSpec.getSize(widthMeasureSpec)

        //todo 确定布满屏幕宽度所需比例，绘制的时候设置按照这个比例来
        mapScale = width/mapRect.width()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

    }
}