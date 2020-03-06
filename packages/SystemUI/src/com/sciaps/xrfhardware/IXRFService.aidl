package com.sciaps.xrfhardware;

import com.sciaps.xrfhardware.IXRFServiceCallback;
import com.sciaps.xrfhardware.IXRFHandle;

interface IXRFService
{
    void addCallback(in IXRFServiceCallback callback);
    void removeCallback(in IXRFServiceCallback callback);
    int getArmStatus();
    void setArmStatus(int status);
    int enableTube(in IXRFHandle handle, float keV, float uA);
    int disableTube();
    int startDSP(int newRunFlag);
    int stopDSP();
    int i2cRead(int address, int reg, out byte[] response);
    int i2cWrite(int address, int reg, in byte[] payload);
    byte[] dppCommand(int command, in byte[] payload, out byte[] response);
    int setDPPBaudrate(int baud);
    int readSpectrum(out double[] dataMCA, out double[] energyCalParams, out float[] times, out int[] counts);
    String readMetadata(in String key);
    int writeMetadata(in String key, in String value);
    String versionInfo();
    int setNoseCamLedBrightness(int percent);
    int getNoseCamLedBrightness();
}
