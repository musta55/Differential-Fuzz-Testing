@Override
public String toString() {
    switch(_type) {
        case PLUS_MULT:
            return "t(+*)";
        case MINUS_MULT:
            return "t(-*)";
        case BIASADD:
            return "t(bias+)";
        case BIASMULT:
            return "t(bias*)";
        case REPLACE:
        case REPLACE_NAN:
            return "t(rplc)";
        case IFELSE:
            return "t(ifelse)";
        case LOOKUP_RC1:
            return "u(ixrc1)";
        case LOOKUP_RVECT1:
            return "u(ixrv1)";
        default:
            return super.toString();
    }
}