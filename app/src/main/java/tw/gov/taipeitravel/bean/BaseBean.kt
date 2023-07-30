package tw.gov.taipeitravel.bean

import java.io.Serializable

open class BaseBean : Cloneable, Serializable {

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): Any {
        return super.clone()
    }

}
