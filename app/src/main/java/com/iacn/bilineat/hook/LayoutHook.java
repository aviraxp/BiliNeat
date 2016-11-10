package com.iacn.bilineat.hook;

import android.content.res.XResources;
import android.text.util.Linkify;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iacn.bilineat.Constant;

import de.robv.android.xposed.callbacks.XC_LayoutInflated;

import static com.iacn.bilineat.XposedInit.xSharedPref;

/**
 * Created by iAcn on 2016/10/5
 * Emali iAcn0301@foxmail.com
 */

public class LayoutHook {

    /**
     * 布局Hook的具体实现方法
     *
     * @param res Xposed 的 XResources
     */
    public void doHook(XResources res) {
        // 去除发现里的兴趣圈
        if (xSharedPref.getBoolean("found_group", false)) {
            res.hookLayout(Constant.biliPackageName, "layout", "bili_app_fragment_discover", new XC_LayoutInflated() {
                @Override
                public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                    RelativeLayout rl = (RelativeLayout) layoutInflatedParam.view.findViewById(layoutInflatedParam.res
                            .getIdentifier("group", "id", Constant.biliPackageName));
                    rl.setVisibility(View.GONE);
                }
            });
        }

        // 将评论里的部分网址转换为可点击的链接
        if (xSharedPref.getBoolean("auto_link", false)) {
            res.hookLayout(Constant.biliPackageName, "layout", "bili_app_layout_list_item_feedback_item_include",
                    new XC_LayoutInflated() {
                        @Override
                        public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                            TextView textView = (TextView) layoutInflatedParam.view.findViewById(layoutInflatedParam.res
                                    .getIdentifier("message", "id", Constant.biliPackageName));
                            textView.setAutoLinkMask(Linkify.WEB_URLS);
                        }
                    });
        }

        // 去除推荐里的游戏中心
        res.hookLayout(Constant.biliPackageName, "layout", "bili_app_index_more_game", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                TextView game = (TextView) layoutInflatedParam.view.findViewById(layoutInflatedParam.res
                        .getIdentifier("more_action", "id", Constant.biliPackageName));
                game.setVisibility(View.GONE);
            }
        });

        // 添加 1080P 优先
        if (xSharedPref.getBoolean("default_1080p", false)) {
            res.setReplacement(Constant.biliPackageName, "array", "pref_player_mediaSource_entries",
                    new String[]{
                            "自动选择",
                            "流畅优先",
                            "高清优先",
                            "超清优先",
                            "1080P优先"});
            res.setReplacement(Constant.biliPackageName, "array", "pref_player_mediaSource_entryValues",
                    new String[]{
                            "0",
                            "100",
                            "200",
                            "400",
                            "800"});
        }
    }
}