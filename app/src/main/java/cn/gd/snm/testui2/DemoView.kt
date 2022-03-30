package cn.gd.snm.testui2

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * 自定义view，演示重写onMeasure、onLayout实现对应的xml布局。
 *
 */
class DemoView: ConstraintLayout {
    //todo 测试对应的布局是由img+text组成,所以需要在对象初始化时，初始化这些组件。
    private var img:ImageView = ImageView(context)
    private var tx:TextView = TextView(context)

    init {
        initViews()
    }

    /**
     * 初始化view。
     *
     */
    private fun initViews() {
        img.id = View.generateViewId()
        img.setImageDrawable(context.getDrawable(R.drawable.img))

        tx.id = View.generateViewId()
        tx.setPadding(AppGlobals.px2dp(context,8f),0,0,0)
        tx.text = "大唐女法医"
        tx.setTextColor(context.resources.getColor(R.color.black))
        tx.textSize = 30f
        tx.gravity = Gravity.CENTER_VERTICAL
        tx.setBackgroundColor(context.resources.getColor(R.color.purple_200))

        this.setBackgroundColor(context.resources.getColor(R.color.black))
    }

    constructor(context: Context) : super(context)


    constructor(context: Context, width:Int, height:Int):super(context){
        var params = ViewGroup.LayoutParams(width,height)
        this.layoutParams = params


    }

    constructor(context: Context, attributeSet: AttributeSet):super(context,attributeSet)

    /**
     * 确定当前view的大小。
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        //todo: 确定最后的宽高
        setMeasuredDimension(layoutParams.width, layoutParams.height)

    }

    /**
     * 对view进行布局。
     *
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        super.onLayout(changed, left, top, right, bottom)

        //todo 确定LayoutParams用的是哪个布局的。
        addView(img)
        img.layout(0,0,AppGlobals.dp2px(context,270f),AppGlobals.dp2px(context,380f))

        addView(tx)
        tx.layout(0,AppGlobals.dp2px(context,380f),AppGlobals.dp2px(context,270f),
            AppGlobals.dp2px(context,440f))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}