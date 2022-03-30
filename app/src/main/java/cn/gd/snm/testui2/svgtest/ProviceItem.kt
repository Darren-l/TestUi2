package cn.gd.snm.testui2.svgtest

import android.graphics.*

/**
 * DarrenAdd：处理具体绘制path
 *
 */
class ProviceItem(var path: Path) {
    //默认的绘制颜色
    var color = Color.WHITE
    /**
     * 封装当前path的绘制操作。
     */
    fun drawPath(canvas:Canvas, paint: Paint){
        //先绘制图形
        paint.strokeWidth = 1f
        paint.style = Paint.Style.FILL
        paint.color = color
        canvas.drawPath(path, paint)

        //再描边界
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        canvas.drawPath(path, paint)
    }

    /**
     * 判断点是否在当前区域内
     */
    fun isContain(x: Float,y:Float):Boolean{
        var rectf = RectF()
        path.computeBounds(rectf,true)

        var region = Region()
        region.setPath(path, Region(rectf.left.toInt(),rectf.top.toInt(),
            rectf.right.toInt(),rectf.right.toInt()))

        return region.contains(x.toInt(),y.toInt())
    }
}