package com.sciaps.xrfhardware;

interface IXRFServiceCallback
{
    void onTriggerStateChanged(int value);
    void onArmedStateChanged(int value);
}
