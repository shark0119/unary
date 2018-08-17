package cn.com.unary.initcopy.mock;

import cn.com.unary.initcopy.dao.FileManager;

public class Mock {

	public static class Transfer {
		public static int MAX_SIZE;
		public void sendData (byte[] data) throws Exception{
		}
	}
	public static Transfer newTransfer () {
		return new Transfer();
	}
	/*public static InfceClientInit getCI () {
		return null;
	}
	public static InfceReadPackFromFile getRPFF (SyncType syncType) {
		if (syncType.equals(SyncType.SYNC_ALL)) {
			
		} else if (syncType.equals(SyncType.SYNC_DATA_DIFF)) {
			
		} else if (syncType.equals(SyncType.SYNC_TIME_DIFF)) {
			
		}
		return null;
	}*/
	public static FileManager getIFM() {
		return null;
	}
}
