package com.apartment.domain.apartment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApartmentType {
    STUDIO("Studio"),
    ONE_BEDROOM("1 phòng ngủ"),
    TWO_BEDROOM("2 phòng ngủ"),
    TWO_BEDROOM_PLUS("2 phòng ngủ+"),
    THREE_BEDROOM("3 phòng ngủ"),
    THREE_BEDROOM_PLUS("3 phòng ngủ+"),
    PENTHOUSE("Penthouse"),
    DUPLEX("Duplex"),
    OTHER("Khác");

    private final String label;
}
