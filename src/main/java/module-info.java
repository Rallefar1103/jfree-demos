module org.jfree.demos {
    requires java.desktop;
    requires java.logging;
    requires org.jfree.svg;
    requires org.jfree.pdf;
    requires org.jfree.jfreechart_dlf;
    // requires org.jfree.chart3d;
    requires com.formdev.flatlaf;

    // exports com.orsoncharts.demo;
    exports org.jfree.pdf.demo;
    exports org.jfree.chart.demo2;
}
