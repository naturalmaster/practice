package com.example.coney.calc_formula.dataManage.operation.type;

import com.example.coney.calc_formula.dataManage.data.font.Font;

/**
 *
 * @author coney
 * @date 2018/12/10
 */

public class FontState extends Operation {
    /**
     * 字体对象
     */
    public Font font;

    public FontState(float xOffset,float yOffset) {
        super(com.example.coney.calc_formula.dataManage.operation.type.OperationType.ALTER_FONT,xOffset,yOffset);
    }
}
