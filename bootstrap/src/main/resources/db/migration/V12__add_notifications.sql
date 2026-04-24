CREATE TABLE notifications (
    id          UUID PRIMARY KEY,
    title       VARCHAR(255)  NOT NULL,
    message     TEXT,
    type        VARCHAR(50),
    target_id   VARCHAR(255),
    is_read     BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP     NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_notifications_created_at ON notifications (created_at DESC);
CREATE INDEX idx_notifications_is_read    ON notifications (is_read);
