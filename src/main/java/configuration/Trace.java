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
            "cambridge/msr-cambridge1-sample.csv",
            new CambridgeTraceParser()),
    ARC(
            "arc/OLTP.lis",
            new ArcTraceParser()),
    UPENN(
            "upenn/aligned.trace",
            new UpennTraceParser()),
    RobinHood(
            "robinHood/robinhood.json",
            new RobinHoodTraceParser()),
    AdaptSize(
            ("adaptsize/request.trace"),
            new AdaptSizeParser()),
    UMASS(
            ("umass/WebSearch1.spc"),
            new UmassTraceParser());

    private String filePath;
    private AbstractParserClass parser;

    Trace(String filePath, AbstractParserClass parser) {
        String defaultPath = "resources/traces/";
        this.filePath = defaultPath + filePath;
        this.parser = parser;
    }

    public String getFilePath() {
        return filePath;
    }

    public AbstractParserClass getParser() {
        return parser;
    }
}
