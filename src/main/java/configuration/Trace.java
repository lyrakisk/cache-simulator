package configuration;

import parser.AbstractParserClass;
import parser.arc.ArcTraceParser;
import parser.robinHood.RobinHoodTraceParser;
import parser.snia.CambridgeTraceParser;
import parser.upenn.UpennTraceParser;

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
            new RobinHoodTraceParser());

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
