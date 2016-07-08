package com.aigestudio.wheelpicker;

import android.graphics.Typeface;

import java.util.List;

/**
 * 滚轮控件对外功能接口
 * Interface of WheelView's function you can use
 *
 * @author AigeStudio 2015-12-03
 * @author AigeStudio 2015-12-08
 * @author AigeStudio 2015-12-12
 * @version 1.0.0 beta
 */
public interface IWheelPicker {
    /**
     * 设置显示数据
     * 目前为止WheelPicker仅仅支持字符串列表类型的数据�?
     * Set data to display in WheelView
     * So far, WheelPicker only support string list for data set
     *
     * @param data 显示数据
     *             Display data set
     */
    void setData(List<String> data);

    /**
     * 设置滚动监听
     * Set mListener
     *
     * @param listener 滚动监听�?
     */
    void setOnWheelChangeListener(AbstractWheelPicker.OnWheelChangeListener listener);

    /**
     * 设置当前居中显示的文本在数据列表中的下标�?
     * Set index of data in list will display in WheelView center
     *
     * @param index 下标�?
     *              Index of data in list will display in WheelView center
     */
    void setItemIndex(int index);

    /**
     * 设置Item间距
     * Set space of items
     *
     * @param space Item间距大小
     *              Space of items
     */
    void setItemSpace(int space);

    /**
     * 设置显示的Item个数
     * Set count of item display
     *
     * @param count Item显示的个�?
     *              Count of item display
     */
    void setItemCount(int count);

    /**
     * 设置文本颜色
     * Set item text color
     *
     * @param color 文本颜色
     *              Item text color
     */
    void setTextColor(int color);

    /**
     * 设置文本大小
     * Set size of text
     *
     * @param size 文本大小
     *             Text size
     */
    void setTextSize(int size);

    /**
     * �?套字�?
     * Set typeface
     *
     * @param typeface 字体
     *             Typeface
     */
    void setTypeface(Typeface typeface);

    /**
     * 清除缓存
     * 在某些子类实现中为了加�?�绘制减少�?�能损�?�会将一些计算结果进行缓存，当影响这些计算结果的参数发生改变时需要清除这些缓存并重建
     * Clear calculate cache
     * WheelPicker will cache calculate result in some specific implementation, when the parameter influences the calculated result changed, we need to clean these cache and rebuild them.
     * This method will help to how to clean the cache.
     */
    void clearCache();

    /**
     * 设置当前Item文本的颜�?
     * Set color of text in current item
     *
     * @param color 16进制的颜色�?? 例如�?0xFF456789
     *              Color in hex value like 0xFF456789
     */
    void setCurrentTextColor(int color);

    /**
     * 设置当前Item项的装饰�?
     * Set decorate of current item
     *
     * @param ignorePadding 是否忽略WheelView的内边距
     *                      Is ignore padding of WheelView
     * @param decor         装饰物对�?
     *                      Subclass of AbstractWheelDecor{@link AbstractWheelDecor}
     */
    void setWheelDecor(boolean ignorePadding, AbstractWheelDecor decor);

//    /**
//     * 设置WheelPicker每个Item中的文本宽高是否都一�?
//     * 默认情况下WheelPicker会遍历数据集中的每一个Item项来计算确定自身的宽高尺寸，当每个Item项的宽高都一致时（比�?1900-2100这样的年份数据）对整个数据集遍历显然没必要，此时就可以�?�过该方法传入true来表示每�?个Item项都应该有相同的尺寸大小，这样可以在数据较多的情况下减轻计算损�??
//     * Set is every item of WheelPicker has same width and height
//     *
//     * @param hasSameSize true表示每个Item中的文本宽高都一样，否则反之
//     *                    true means every item of WheelPicker has same width and height, otherwise.
//     */
//    @Deprecated
//    void setHasSameSize(boolean hasSameSize);
}