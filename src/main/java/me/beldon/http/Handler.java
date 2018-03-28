package me.beldon.http;

/**
 * @author Beldon
 */
public interface Handler {

    /**
     * 初始化handler
     * @param:  @param context
     * @return: void
     * @Autor: Han
     */
    public void init(Context context);

    /**
     * handler service(service应该不是这样做的... - -!)
     * @param:  @param context
     * @return: void
     * @Autor: Han
     */
    public void service(Context context);

    /**
     * Get形式执行该方法
     * @param:  @param context
     * @return: void
     * @Autor: Han
     */
    public void doGet(Context context);

    /**
     * POST形式执行该方法
     * @param:  @param context
     * @return: void
     * @Autor: Han
     */
    public void doPost(Context context);

    /**
     * 销毁Handler(并没有销毁... - -!)
     * @param:  @param context
     * @return: void
     * @Autor: Han
     */
    public void destory(Context context);
}