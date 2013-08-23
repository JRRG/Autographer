package com.omg.autographer.device;

public class Command {

	private final String name;
	private final int code;

	// tags for the
	private static final String GET = "Get";
	private static final String SET = "Set";
	private static final String RET = "Ret";
	private static final String DELETE = "Delete";

	// tags for all command/package names
	public static final String ERROR = "Error";
	public static final String GET_IMAGE_COUNT = "GetImageCount";
	public static final String RET_IMAGE_COUNT = "RetGetImageCount";
	public static final String GET_IMAGE = "GetImage";
	public static final String RET_IMAGE = "RetImage";
	public static final String DELETE_IMAGE = "DeleteImage";
	public static final String RET_DELETE_IMAGE = "RetDeleteImage";
	public static final String DELETE_ALL = "DeleteAll";
	public static final String RET_DELETE_ALL = "RetDeleteAll";
	public static final String GET_IMAGE_METADATA = "GetImageMetadata";
	public static final String RET_IMAGE_METADATA = "RetImageMetadata";
	public static final String SET_TAGS = "SetTags";
	public static final String RET_SET_TAGS = "RetSetTags";
	public static final String GET_IMAGE_DATA_HASH = "GetImageDataHash";
	public static final String RET_IMAGE_DATA_HASH = "RetImageDataHash";
	public static final String GET_COLLECTIONS = "GetCollections";
	public static final String RET_COLLECTIONS = "RetCollections";
	public static final String GET_BASIC_IMAGE_METADATA = "GetBasicImageMetadata";
	public static final String RET_BASIC_IMAGE_METADATA = "RetBasicImageMetadata";
	public static final String SET_FAVOURITE = "SetFavourite";
	public static final String RET_SET_FAVOURITE = "RetSetFavourite";
	public static final String SET_CAPTURE_RATE = "SetCaptureRate";
	public static final String RET_SET_CAPTURE_RATE = "RetSetCaptureRate";
	public static final String GET_CAPTURE_RATE = "GetCaptureRate";
	public static final String RET_CAPTURE_RATE = "RetCaptureRate";
	public static final String SET_SOUND = "SetSound";
	public static final String RET_SET_SOUND = "RetSetSound";
	public static final String GET_SOUND_ENABLED = "GetSoundEnabled";
	public static final String RET_SOUND_ENABLED = "RetSoundEnabled";
	public static final String SET_FLIGHT_MODE = "SetFlightMode";
	public static final String RET_SET_FLIGHT_MODE = "RetSetFlightMode";
	public static final String GET_FLIGHT_MODE = "GetFlightMode";
	public static final String RET_FLIGHT_MODE = "RetFlightMode";
	public static final String GET_GPS_ENABLED = "GetGPSEnabled";
	public static final String RET_GPS_ENABLED = "RetGPSEnabled";
	public static final String GET_BATTERY_INFO = "GetBatteryInfo";
	public static final String RET_BATTERY_INFO = "RetBatteryInfo";
	public static final String GET_STORAGE_INFO = "GetStorageInfo";
	public static final String RET_STORAGE_INFO = "RetStorageInfo";
	public static final String RESET_PAIRING_HISTORY = "ResetPairingHistory";
	public static final String RET_RESET_PAIRING_HISTORY = "RetResetPairingHistory";
	public static final String GET_PAIRED_DEVICE_COUNT = "GetPairedDeviceCount";
	public static final String RET_PAIRED_DEVICE_COUNT = "RetPairedDeviceCount";
	public static final String GET_DEVICE_ID = "GetDeviceID";
	public static final String RET_DEVICE_ID = "RetDeviceID";
	public static final String GET_DEVICE_NAME = "GetDeviceName";
	public static final String RET_DEVICE_NAME = "RetDeviceName";
	public static final String SET_DEVICE_NAME = "SetDeviceName";
	public static final String RET_SET_DEVICE_NAME = "GetSetDeviceName";
	public static final String GET_FIRMWARE_VERSION = "GetFirmwareVersion";
	public static final String RET_FIRMWARE_VERSION = "RetFirmwareVersion";
	public static final String GET_BLUETOOTH_ADDRESS = "GetBluetoothAddress";
	public static final String RET_BLUETOOTH_ADDRESS = "RetBluetoothAddress";
	public static final String GET_API_VERSION = "GetAPIVersion";
	public static final String RET_API_VERSION = "RetAPIVersion";
	public static final String DEVICE_READY = "DeviceReady";
	public static final String RET_DEVICE_READY = "RetDeviceReady";
	public static final String GET_TIME = "GetTime";
	public static final String RET_TIME = "RetTime";
	public static final String SET_TIME = "SetTime";
	public static final String RET_SET_TIME = "RetSetTime";
	public static final String IS_AUTHORISED = "IsAuthorised";
	public static final String RET_IS_AUTHORISED = "RetIsAuthorised";
	public static final String AUTHORISE_WITH_CODE = "AuthoriseWithCode";
	public static final String RET_AUTHORISE_WITH_CODE = "RetAuthoriseWithCode";

	public Command(String name, int code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public int getCode() {
		return code;
	}

	public boolean isGet() {
		return name.startsWith(GET);
	}

	public boolean isSet() {
		return name.startsWith(SET);
	}

	public boolean isRet() {
		return name.startsWith(RET);
	}

	public boolean isDelete() {
		return name.startsWith(DELETE);
	}
}
