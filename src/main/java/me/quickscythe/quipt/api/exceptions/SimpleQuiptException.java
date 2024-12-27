package me.quickscythe.quipt.api.exceptions;

import me.quickscythe.quipt.utils.CoreUtils;

public class SimpleQuiptException extends Throwable {

    public SimpleQuiptException(String message) {
        super(message);
        CoreUtils.logger().log("SimpleQuiptException", message);

    }
}
