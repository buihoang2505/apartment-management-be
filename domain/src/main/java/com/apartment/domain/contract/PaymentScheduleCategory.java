package com.apartment.domain.contract;

public enum PaymentScheduleCategory {
    /** Kỳ thu tiền theo hợp đồng (thuê tháng / trả góp). */
    INSTALLMENT,
    /** Kỳ thu tiền cọc (trước khi hợp đồng có hiệu lực). */
    DEPOSIT,
    /** Kỳ hoàn trả tiền cọc (sau khi hợp đồng kết thúc). */
    DEPOSIT_REFUND
}
