package cn.com.unary.initcopy.entity;

public final class Constants {

	public static class GlobalVar {
		public static int MAX_PACK_SIZE = 4096;
	}
	private Constants () {}
	
	public enum RequestType {
		ADD, QUERY, DELETE,
	}
	public enum FileType {
		REGULAR_FILE, DIR, SYMBOLIC_LINK, OTHER,
	}
	public static enum PackType {
		SYNC_ALL_JAVA(0x01), // 全复制- Java
		RSYNC_JAVA(0x02),	// 差异复制- rsync
		;
		private byte value;
		PackType (int b) {			value = (byte)b;		}
		public static PackType valueOf(byte b) {
			for (PackType type : PackType.values()) {
				if (type.value == b) {
					return type;
				}
			}
			return null;
		}
		public byte getValue () {			return value;		}
	}
}
