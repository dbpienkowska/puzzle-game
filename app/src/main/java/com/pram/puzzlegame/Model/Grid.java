package com.pram.puzzlegame.Model;

import com.bumptech.glide.Glide;
import com.pram.puzzlegame.R;
import com.pram.puzzlegame.Utility.LayoutWrapper;
import com.pram.puzzlegame.Utility.Position;

import static com.pram.puzzlegame.Utility.Utils.euclideanDistance;

public class Grid {

    public final int size;
    public final int slotSize;

    public Slot[] slots;
    public Position[] positions;

    public int matched = 0;

    public Grid(int gridSize, int slotSize, LayoutWrapper layout) {
        size = gridSize;
        this.slotSize = slotSize;

        initSlots(size * size, layout);
    }

    public void attachToLayout(LayoutWrapper layout) {
        for(int i = 0; i < slots.length; i++) {
            layout.view.addView(slots[i].view, slots[i].layoutParams);
            Glide.with(layout.view).load(Slot.DEFAULT_DRAWABLE).into(slots[i].view);
        }
    }

    public boolean isInRange(Position position) {
        Position firstSlot = positions[0];
        Position lastSlot = positions[positions.length - 1];
        Position lastSlotBottomRight = new Position(lastSlot.x + slotSize, lastSlot.y + slotSize);

        return position.x + slotSize > firstSlot.x && position.x < lastSlotBottomRight.x && position.y < lastSlotBottomRight.y;
    }

    public int getNearestIndex(Position source) {
        int nearest = 0;
        double minDistance = euclideanDistance(source, positions[nearest]);

        for(int i = 1; i < positions.length; i++) {
            double distance = euclideanDistance(source, positions[i]);
            if(distance < minDistance) {
                minDistance = distance;
                nearest = i;
            }
        }

        return nearest;
    }

    public void unFocusAllSlots() {
        for(Slot s : slots) {
            s.unFocus();
        }
    }

    private void initSlots(int count, LayoutWrapper layout) {
        slots = new Slot[count];
        positions = new Position[count];

        int xOffset = (layout.width - 4 * slotSize) / 2;
        int yOffset = 2 * layout.margin;
        yOffset += layout.view.findViewById(R.id.puzzleChronometer).getLayoutParams().height;

        for(int y = 0; y < size; y++) {
            for(int x = 0; x < size; x++) {
                int i = size * y + x;
                Position position = new Position(xOffset + x * slotSize, yOffset + y * slotSize);
                Slot slot = new Slot(i, slotSize, layout.context);
                slot.setPosition(position);

                positions[i] = position;
                slots[i] = slot;
            }
        }
    }
}
