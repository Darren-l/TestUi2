package cn.gd.snm.testui2

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import java.security.Policy

/**
 * 创建一个工厂，通过工厂来创建view
 *
 */
class DemoFactory {

    companion object{

        /**
         * java创建view需要注意：
         *  1. 通过构造方法创建的view，默认params对象为空。需要创建params后赋值给view。
         *  2. 默认的view id为-1，需要调用generateViewId给view赋值id，否则后面布局的时候如果是-1，
         *  那么布局会失败。
         *
         * 如果宽高对应的xml设置的值为wrap_content或match_parent，那么代码中也一样可以这么赋值，对应的是
         * ViewGroup.LayoutParams.WRAP_CONTENT和ViewGroup.LayoutParams.MATCH_PARENT。因为会在
         * onMeasure时期去处理这个值实际对应的宽度。
         *
         * 每个Layout对应都会有个LayoutParams类，里面封装关乎这个Layout的定制接口，这也不难理解，因为每一种布局
         * 方式是不一样的。如ConstraintLayout那么对应的就是ConstraintLayout.LayoutParams。
         *
         */
        fun createDemoView(context: Context): View {
            var demo = ConstraintLayout(context)
            //todo 必须要给定id，布局的时候要用。
            demo.id = View.generateViewId()

            //todo 对img进行布局，把xml中的代码对应用java实现。
            var img = ImageView(context)
            img.id = View.generateViewId()


            var width = AppGlobals.dp2px(context,270f)
            var height = AppGlobals.dp2px(context,380f)
            var params = ConstraintLayout.LayoutParams(width,height)

            params.leftToLeft = demo.id
            params.rightToRight = demo.id
            params.topToTop = demo.id
            img.layoutParams = params
            img.setImageDrawable(context.getDrawable(R.drawable.img))


            //todo 对tx进行布局
            var tx = TextView(context)
            tx.id = View.generateViewId()

            var widthTx = AppGlobals.dp2px(context,270f)
            var heightTx = AppGlobals.dp2px(context,60f)
            var paramsTx = ConstraintLayout.LayoutParams(widthTx, heightTx)
            paramsTx.leftToLeft = demo.id
            paramsTx.rightToRight = demo.id
            paramsTx.topToBottom = img.id
            tx.layoutParams = paramsTx
            tx.setPadding(AppGlobals.dp2px(context,8f),0,0,0)
            tx.text = "大唐女法医"
            tx.setTextColor(context.resources.getColor(R.color.black))
            tx.textSize = 30f
            tx.gravity = Gravity.CENTER_VERTICAL
            tx.setBackgroundColor(context.resources.getColor(R.color.purple_200))

            //todo 对父容器进行布局
            var paramsDemo = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            demo.layoutParams = paramsDemo

            //todo 最后别忘了把view add进容器布局中。
            demo.addView(img)
            demo.addView(tx)

            return demo
        }
    }




}