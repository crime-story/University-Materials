package com.lifeSavers.emergencyapp.model

class Message(var message: String? = null, var senderId: String? = null) {
    var messageId: String? = null
    var imageUrl: String? = null

    constructor() : this(null, null)
}
