package configuration;

import data.parser.AbstractParserClass;
import data.parser.adaptsize.AdaptSizeParser;
import data.parser.arc.ArcTraceParser;
import data.parser.robinhood.RobinHoodTraceParser;
import data.parser.snia.CambridgeTraceParser;
import data.parser.umass.UmassTraceParser;
import data.parser.upenn.UpennTraceParser;

public enum Trace {
    Cambridge(
            "src/main/resources/traces/cambridge/msr-cambridge1-sample.csv",
            new CambridgeTraceParser()),
    ARC(
            "src/main/resources/traces/arc/OLTP.lis",
            new ArcTraceParser()),
    UPENN(
            "src/main/resources/traces/upenn/aligned.trace",
            new UpennTraceParser()),
    RobinHood(
            "src/main/resources/traces/robinHood/robinhood.json",
            new RobinHoodTraceParser()),
    AdaptSize(
            ("src/main/resources/traces/adaptsize/request.trace"),
            new AdaptSizeParser()),
    UMASS(
            ("src/main/resources/traces/umass/WebSearch1.spc"),
            new UmassTraceParser());

    private String filePath;
    private AbstractParserClass parser;

    Trace(String filePath, AbstractParserClass parser) {
        this.filePath = filePath;
        this.parser = parser;
    }

    public String getFilePath() {
        return filePath;
    }

    public AbstractParserClass getParser() {
        return parser;
    }
}
