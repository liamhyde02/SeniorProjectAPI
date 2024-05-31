package helloworld.Patterns;

abstract class PatternChainLink implements PatternChain {
    PatternChain nextLink;

    public PatternChainLink(PatternChain nextLink) {
        this.nextLink = nextLink;
    }

    public abstract void ParsePattern();
}
