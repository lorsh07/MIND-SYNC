package mindsync.gui;

import javax.swing.*;
import java.awt.*;

// 위에서 아래로 쌓는 패널. 항목은 가로로 꽉 차고, 높이는 자기 크기만큼만 차지한다.
public class Stack extends JPanel {
    private final int gap;
    private int row = 0;

    public Stack(int gap) {
        super(new GridBagLayout());
        this.gap = gap;
        setOpaque(false);
    }

    public Stack add(Component c) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = row;
        gc.weightx = 1;
        gc.weighty = 0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.insets = new Insets(row == 0 ? 0 : gap, 0, 0, 0);
        super.add(c, gc);
        row++;
        return this;
    }

    // 남는 공간을 이 항목이 모두 차지한다
    public Stack addFill(Component c) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = row;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(row == 0 ? 0 : gap, 0, 0, 0);
        super.add(c, gc);
        row++;
        return this;
    }

    // 항목들을 위쪽에 붙여 두기 위한 빈 공간
    public Stack finish() {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = row;
        gc.weighty = 1;
        super.add(Box.createGlue(), gc);
        row++;
        return this;
    }
}
