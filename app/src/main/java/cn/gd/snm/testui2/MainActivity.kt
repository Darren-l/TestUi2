package cn.gd.snm.testui2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        testXml()

//        ly_body.removeAllViews()

//        testJavaImp()

        testJavaImp2()
    }

    private fun testJavaImp2() {
        var width = AppGlobals.dp2px(this,270f)
        var height = AppGlobals.dp2px(this,440f)

        var demoView = DemoView(this, width, height)
        ly_body.addView(demoView)
    }

    /**
     * java实现1
     *
     */
    private fun testJavaImp() {
        var before = System.currentTimeMillis()
        var demoView = DemoFactory.createDemoView(this)

        demoView.setBackgroundColor(resources.getColor(R.color.black))
        ly_body.addView(demoView)
        Log.d("darrenTest","time22 = ${System.currentTimeMillis() - before}")
    }

    /**
     * 使用xml的方式加载一个布局。
     *
     */
    private fun testXml() {
        var before = System.currentTimeMillis()
        var demoLy = LayoutInflater.from(this).inflate(R.layout.demo,null,false)
        ly_body.addView(demoLy)
        Log.d("darrenTest","time = ${System.currentTimeMillis() - before}")
    }
}