package cn.zzfyip.search.common.constant;

public class PatentConstants {
    
    public static Short TYPE_01_FAMING = new Short("11");
    public static Short TYPE_02_SHIYONGXINXING = new Short("22");
    public static Short TYPE_03_WAIGUANSHEJI = new Short("33");
    
	public static String STATUS_01_INIT = "INIT";
	public static String STATUS_02_GONGKAI = "GONGKAI";
	public static String STATUS_03_SHENCHA = "SHENCHA";
	public static String STATUS_04_SHOUQUAN = "SHOUQUAN";
	public static String STATUS_05_CHEHUI = "CHEHUI";
	
	/**
	 * 发文检索状态：正常状态，需要检索
	 */
	public static String FAWEN_STATUS_01_NORMAL = "NORMAL";
	/**
	 * 发文检索状态：成功状态，已经完成检索
	 */
	public static String FAWEN_STATUS_02_DABIAN = "DABIAN";
	/**
	 * 发文检索状态：成功状态，已经完成检索
	 */
	public static String FAWEN_STATUS_03_BOHUI = "BOHUI";
	/**
	 * 发文检索状态：不需检索状态，专利不需要进行发文通知检索（有代理机构 or 包含过滤词 or 专利权终止）
	 */
	public static String FAWEN_STATUS_04_NOSEARCH = "NOSEARCH";
}
