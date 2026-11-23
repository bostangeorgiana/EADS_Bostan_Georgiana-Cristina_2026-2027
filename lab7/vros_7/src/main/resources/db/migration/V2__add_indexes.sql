-- sunt indexi pt cautari frecvente pe livrari
CREATE INDEX idx_deliveries_customer_id ON deliveries(customer_id);
CREATE INDEX idx_deliveries_created_at ON deliveries(created_at);