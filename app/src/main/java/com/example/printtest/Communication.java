package com.example.printtest;

import androidx.core.util.Pair;

import com.starmicronics.stario.StarPrinterStatus;
import com.starmicronics.stario.StarResultCode;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"UnusedParameters", "UnusedAssignment", "WeakerAccess"})
public class Communication {

    public static class CommunicationResult {
        private Result mResult = Result.ErrorUnknown;
        private int    mCode   = StarResultCode.FAILURE;

        public CommunicationResult(Result result, int code) {
            mResult = result;
            mCode   = code;
        }

        public Result getResult() {
            return mResult;
        }

        public int getCode() {
            return mCode;
        }
    }

    public enum Result {
        Success,
        ErrorUnknown,
        ErrorOpenPort,
        ErrorBeginCheckedBlock,
        ErrorEndCheckedBlock,
        ErrorWritePort,
        ErrorReadPort,
    }

    public enum PresenterStatus {
        NoPaper,
        Loop,
        Hold,
        Retract,
        Eject
    }

    interface StatusCallback {
        void onStatus(StarPrinterStatus result);
    }

    interface FirmwareInformationCallback {
        void onFirmwareInformation(Map<String, String> firmwareInformationMap);
    }

    interface SerialNumberCallback {
        void onSerialNumber(CommunicationResult communicationResult, String serialNumber);
    }

    interface SendCallback {
        void onStatus(CommunicationResult communicationResult);
    }

    interface PrintRedirectionCallback {
        void onStatus(List<Pair<String, CommunicationResult>> communicationResultList);
    }

    interface PresenterStateCheckCallback {
        void onStatus(Communication.PresenterStatus presenterStatus, StarPrinterStatus status);
    }

    /*public static void sendCommands(Object lock, byte[] commands, StarIOPort port, int endCheckedBlockTimeout, SendCallback callback) {
        SendCommandThread thread = new SendCommandThread(lock, commands, port, endCheckedBlockTimeout, callback);
        thread.start();
    }

    public static void sendCommandsDoNotCheckCondition(Object lock, byte[] commands, StarIOPort port, SendCallback callback) {
        SendCommandDoNotCheckConditionThread thread = new SendCommandDoNotCheckConditionThread(lock, commands, port, callback);
        thread.start();
    }

    public static void sendCommands(Object lock, byte[] commands, String portName, String portSettings, int timeout, int endCheckedBlockTimeout, Context context, SendCallback callback) {
        SendCommandThread thread = new SendCommandThread(lock, commands, portName, portSettings, timeout, endCheckedBlockTimeout, context, callback);
        thread.start();
    }

    public static void sendCommandsDoNotCheckCondition(Object lock, byte[] commands, String portName, String portSettings, int timeout, Context context, SendCallback callback) {
        SendCommandDoNotCheckConditionThread thread = new SendCommandDoNotCheckConditionThread(lock, commands, portName, portSettings, timeout, context, callback);
        thread.start();
    }

    public static void sendCommandsForRedirection(Object lock, byte[] commands, List<PrinterSettings> printerSettingsList, int timeout, int endCheckedBlockTimeout, Context context, PrintRedirectionCallback callback) {
        SendCommandForRedirectionThread thread = new SendCommandForRedirectionThread(lock, commands, printerSettingsList, timeout, endCheckedBlockTimeout, context, callback);
        thread.start();
    }

    public static void sendCommandsWithPresenterStateCheck(Object lock, byte[] commands, String portName, String portSettings, int timeout, int endCheckedBlockTimeout, Context context, SendCallback sendCallback, PresenterStateCheckCallback presenterStateCallback) {
        SendCommandWithPresenterStateCheckThread thread = new SendCommandWithPresenterStateCheckThread(lock, commands, portName, portSettings, timeout, endCheckedBlockTimeout, context, sendCallback, presenterStateCallback);
        thread.start();
    }

    public static void retrieveStatus(Object lock, String portName, String portSettings, int timeout, Context context, StatusCallback callback) {
        RetrieveStatusThread thread = new RetrieveStatusThread(lock, portName, portSettings, timeout, context, callback);
        thread.start();
    }

    public static void getFirmwareInformation(Object lock, String portName, String portSettings, int timeout, Context context, FirmwareInformationCallback callback) {
        GetFirmwareInformationThread thread = new GetFirmwareInformationThread(lock, portName, portSettings, timeout, context, callback);
        thread.start();
    }

    public static void getSerialNumber(Object lock, String portName, String portSettings, int timeout, Context context, SerialNumberCallback callback) {
        GetSerialNumberThread thread = new GetSerialNumberThread(lock, portName, portSettings, timeout, context, callback);
        thread.start();
    }

    public static void parseDoNotCheckCondition(Object lock, IPeripheralCommandParser parser, String portName, String portSettings, int timeout, Context context, SendCallback callback) {
        ParseDoNotCheckConditionThread thread = new ParseDoNotCheckConditionThread(lock, parser, portName, portSettings, timeout, context, callback);
        thread.start();
    }

    public static void parseDoNotCheckCondition(Object lock, IPeripheralCommandParser parser, StarIOPort port, SendCallback callback) {
        ParseDoNotCheckConditionThread thread = new ParseDoNotCheckConditionThread(lock, parser, port, callback);
        thread.start();
    }

    public static void setUsbSerialNumber(Object lock, byte[] usbSerialNumber, boolean enable, String portName, String portSettings, int timeout, Context context, SendCallback callback) {
        SetUsbSerialNumberThread thread = new SetUsbSerialNumberThread(lock, usbSerialNumber, enable, portName, portSettings, timeout, context, callback);
        thread.start();
    }

    public static void initializeUsbSerialNumber(Object lock, boolean enable, String portName, String portSettings, int timeout, Context context, SendCallback callback) {
        InitializeUsbSerialNumberThread thread = new InitializeUsbSerialNumberThread(lock, enable, portName, portSettings, timeout, context, callback);
        thread.start();
    }*/

    public static String getCommunicationResultMessage(CommunicationResult communicationResult) {
        StringBuilder builder = new StringBuilder();

        switch (communicationResult.getResult()) {
            case Success:
                builder.append("Success!");
                break;
            case ErrorOpenPort:
                builder.append("Fail to openPort");
                break;
            case ErrorBeginCheckedBlock:
                builder.append("Printer is offline (beginCheckedBlock)");
                break;
            case ErrorEndCheckedBlock:
                builder.append("Printer is offline (endCheckedBlock)");
                break;
            case ErrorReadPort:
                builder.append("Read port error (readPort)");
                break;
            case ErrorWritePort:
                builder.append("Write port error (writePort)");
                break;
            default:
                builder.append("Unknown error");
                break;
        }

        if (communicationResult.getResult() != Result.Success) {
            builder.append("\n\nError code: ");
            builder.append(communicationResult.getCode());

            if (communicationResult.getCode() == StarResultCode.FAILURE) {
                builder.append(" (Failed)");
            }
            else if (communicationResult.getCode() == StarResultCode.FAILURE_IN_USE) {
                builder.append(" (In use)");
            }
        }

        return builder.toString();
    }
}

