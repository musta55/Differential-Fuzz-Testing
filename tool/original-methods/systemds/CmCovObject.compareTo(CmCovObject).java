public int compareTo(CmCovObject that) {
    if (w != that.w)
        return Double.compare(w, that.w);
    else if (mean != that.mean)
        return KahanObject.compare(mean, that.mean);
    else if (m2 != that.m2)
        return KahanObject.compare(m2, that.m2);
    else if (m3 != that.m3)
        return KahanObject.compare(m3, that.m3);
    else if (m4 != that.m4)
        return KahanObject.compare(m4, that.m4);
    else if (mean_v != that.mean_v)
        return KahanObject.compare(mean_v, that.mean_v);
    else if (min != that.min)
        return Double.compare(min, that.min);
    else if (max != that.max)
        return Double.compare(max, that.max);
    else
        return KahanObject.compare(c2, that.c2);
}