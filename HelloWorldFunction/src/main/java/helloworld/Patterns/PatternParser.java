package helloworld.Patterns;

public class PatternParser {
    public static void parsePatterns() {
        PatternChain patternChain = new SingletonPatternStep(null);
        patternChain.ParsePattern();
    }
}
