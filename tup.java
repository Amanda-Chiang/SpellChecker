 public class tup {
	
	public String word;
	public int count;

	public static void main(String [] args) {
	}

	tup(String word, int count) {
		this.word = word;
		this.count = count;
	}

	tup() {
		this.word = "-";
		this.count = 0;
	}

	public boolean has(String w) {
    	return word.equals(w);
    }

    public void setCount(int newC) {
    	this.count = newC;
    }

    public void setWord(String w) {
    	this.word = w;
    }

    public String toString() {
		return word + ": " + count;
	}

	public boolean equals(tup x) {
		return this.word.equals(x.word) && this.count == x.count;
	}
}