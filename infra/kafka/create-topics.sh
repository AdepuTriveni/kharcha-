#!/bin/sh
# Creates Kharcha Kafka topics and their dead-letter topics (spec §7.2). Idempotent.
#
# Usage: create-topics.sh [local|prod]
#   BOOTSTRAP           broker address         (default kafka:19092, i.e. inside docker-compose)
#   REPLICATION_FACTOR  replicas per partition (default 1 for local, 3 for prod)
#   KAFKA_BIN           Kafka CLI directory    (default /opt/kafka/bin, the apache/kafka image)
#
# Keep the table in sync with com.kharcha.common.kafka.Topics (TopicsTest checks it).
set -eu

PROFILE="${1:-local}"
BOOTSTRAP="${BOOTSTRAP:-kafka:19092}"
KAFKA_BIN="${KAFKA_BIN:-/opt/kafka/bin}"

case "$PROFILE" in
  local) DEFAULT_RF=1 ;;
  prod)  DEFAULT_RF=3 ;;
  *) echo "unknown profile: $PROFILE (expected local or prod)" >&2; exit 2 ;;
esac
RF="${REPLICATION_FACTOR:-$DEFAULT_RF}"

DAY_MS=86400000
DLT_RETENTION_DAYS=30

create() {
  # $1 topic, $2 partitions, $3 retention days
  "$KAFKA_BIN/kafka-topics.sh" --bootstrap-server "$BOOTSTRAP" --create --if-not-exists \
    --topic "$1" --partitions "$2" --replication-factor "$RF" \
    --config "retention.ms=$(( $3 * DAY_MS ))"
}

# topic                local  prod  retention-days
TOPICS=$(cat <<'EOF'
raw-events 3 12 30
parsed-transactions 3 12 7
clean-transactions 3 12 7
cash-events 3 12 7
agent-tasks 3 12 3
agent-results 3 12 7
user-feedback 3 6 30
nudge-outcomes 3 6 30
model-shadow 3 3 7
EOF
)

echo "$TOPICS" | while read -r topic local_parts prod_parts days; do
  [ -z "$topic" ] && continue
  if [ "$PROFILE" = prod ]; then parts="$prod_parts"; else parts="$local_parts"; fi
  create "$topic" "$parts" "$days"
  create "$topic.DLT" "$parts" "$DLT_RETENTION_DAYS"   # DLT: same partitions, 30 d (§7.2)
done

echo "Topics on $BOOTSTRAP:"
"$KAFKA_BIN/kafka-topics.sh" --bootstrap-server "$BOOTSTRAP" --list
