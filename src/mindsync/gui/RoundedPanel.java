package mindsync.gui;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private int radius;

    public RoundedPanel(int radius) {
        this.radius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g);
        //extends JPanel — JPanel을 상속받아서, 우리만의 특별한 패널을 만들어요.
        //private int radius; — 모서리를 얼마나 둥글게 할지 저장하는 필드예요.
        //paintComponent(Graphics g) — 이게 핵심이에요! 이 메서드는 **"이 부품을 화면에 실제로 그릴 때 호출되는 메서드"**예요. 지금까지는 JPanel이 기본으로 제공하는 "각진 사각형 그리기"를 그냥 썼는데, 이번엔 이 메서드를 **오버라이드(재정의)**해서 "둥근 사각형을 그려라"로 바꿔치기하는 거예요.
        //Graphics2D g2 = (Graphics2D) g; — Graphics를 더 정교한 그리기가 가능한 Graphics2D로 변환해요. (지금은 "이렇게 변환해서 쓴다"는 정도로 받아들이시면 돼요)
        //g2.setRenderingHint(...) — 그림의 가장자리를 부드럽게(안티앨리어싱) 처리해요. 이게 없으면 둥근 모서리가 계단처럼 삐죽삐죽해 보여요.
        //g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius) — **"이 부품의 전체 너비/높이만큼, radius만큼 둥근 모서리를 가진 사각형을 색칠해서 그려라"**는 뜻이에요.
        //super.paintComponent(g); — 이 패널 안에 들어갈 자식 부품들(글자, 버튼 등)도 마저 정상적으로 그려달라는 뜻이에요. 이게 없으면 배경만 그려지고 안의 내용물이 안 보여요.


    }
}
