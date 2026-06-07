#!/bin/bash
set -e

echo "=== Cortex: Building all services ==="
cd "$(dirname "$0")"

mvn clean package -DskipTests -q

echo ""
echo "=== Starting all services ==="
echo "Gateway         → http://localhost:8080"
echo "  User Service  → http://localhost:8081"
echo "  Catalog       → http://localhost:8082"
echo "  Order Service → http://localhost:8083"
echo "  Notification  → http://localhost:8084"
echo ""

# Start each service in background
java -jar gateway/target/gateway-1.0.0.jar &
java -jar user-service/target/user-service-1.0.0.jar &
java -jar catalog-service/target/catalog-service-1.0.0.jar &
java -jar order-service/target/order-service-1.0.0.jar &
java -jar notification-service/target/notification-service-1.0.0.jar &

echo "All services started. Press Ctrl+C to stop all."
trap 'kill 0' EXIT
wait
