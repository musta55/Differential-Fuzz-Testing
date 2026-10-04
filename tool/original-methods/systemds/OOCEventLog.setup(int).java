public static void setup(int maxNumEvents) {
    _eventTypes = DMLScript.OOC_LOG_EVENTS ? new EventType[maxNumEvents] : null;
    _startTimestamps = DMLScript.OOC_LOG_EVENTS ? new long[maxNumEvents] : null;
    _endTimestamps = DMLScript.OOC_LOG_EVENTS ? new long[maxNumEvents] : null;
    _callerIds = DMLScript.OOC_LOG_EVENTS ? new int[maxNumEvents] : null;
    _threadIds = DMLScript.OOC_LOG_EVENTS ? new long[maxNumEvents] : null;
    _data = DMLScript.OOC_LOG_EVENTS ? new long[maxNumEvents] : null;
}