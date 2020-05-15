package configuration;

import parser.AbstractParserClass;
import parser.arc.ArcTraceParser;
import parser.snia.CambridgeTraceParser;
import parser.upenn.UpennTraceParser;

public enum Trace {
    Cambridge(
            "src/main/resources/traces/cambridge/msr-cambridge1-sample.csv",
            new CambridgeTraceParser()),
    ARC(
            "src/main/resources/traces/arc/OLTP.lis",
            new ArcTraceParser()),
    UPENN(
            "src/main/resources/traces/upenn/aligned.trace",
            new UpennTraceParser());

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
