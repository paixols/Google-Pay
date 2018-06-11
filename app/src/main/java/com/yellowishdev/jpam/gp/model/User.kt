package com.yellowishdev.jpam.gp.model

import java.io.Serializable

/**
 * [User]
 * Represents User Object
 * */
class User(name: String?, email: String?) : Serializable {
    /*Properties*/
    var name: String? = name
    var email: String? = email
    var password: String? = null
    var id: String? = null
    var phone: String? = null
    var countryCode: String? = null
    var zipCode: String? = null
}