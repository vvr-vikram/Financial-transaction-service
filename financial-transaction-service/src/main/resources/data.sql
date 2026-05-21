-- Default fraud rules
INSERT INTO fraud_rule (rule_name, max_amount_limit, enabled) VALUES ('MAX_TXN_AMOUNT', 10000.00, true)
ON DUPLICATE KEY UPDATE max_amount_limit = 10000.00;

-- Some initial accounts for testing
INSERT INTO account (account_id, account_holder_name, account_number, balance, status, created_at, version) 
VALUES ('acc-1111-2222', 'Bala M', '1001001', 5000.00, 'ACTIVE', CURRENT_TIMESTAMP, 0)
ON DUPLICATE KEY UPDATE account_holder_name = 'Bala M';

INSERT INTO account (account_id, account_holder_name, account_number, balance, status, created_at, version) 
VALUES ('acc-3333-4444', 'Vikram V', '1001002', 3000.00, 'ACTIVE', CURRENT_TIMESTAMP, 0)
ON DUPLICATE KEY UPDATE account_holder_name = 'Vikram V';
