package com.firefly.layers.listeners;

import com.firefly.derivative.core.OperationActivation;

public interface InitActivationListener extends java.io.Serializable {
    /**
     * 新建激活函数
     * @return
     */
    OperationActivation newActivation();
}
