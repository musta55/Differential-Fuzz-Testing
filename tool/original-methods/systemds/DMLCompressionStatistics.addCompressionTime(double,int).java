public static void addCompressionTime(double time, int phase) {
    switch(phase) {
        case 0:
            Phase0 += time;
            break;
        case 1:
            Phase1 += time;
            break;
        case 2:
            Phase2 += time;
            break;
        case 3:
            Phase3 += time;
            break;
        case 4:
            Phase4 += time;
            break;
        case 5:
            Phase5 += time;
            break;
    }
}