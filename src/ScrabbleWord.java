import java.io.Serializable;


public class ScrabbleWord implements Serializable, Comparable<ScrabbleWord> {
 
	private static final long serialVersionUID = -5339378108726317860L;
	
	private String wordstr;
	private String word;
	private int value;
	
	@Override
	public String toString() {
		return "["+wordstr+"]";
	}
	public ScrabbleWord(String wordstr) {
		super();
		this.wordstr = wordstr;
		adjustSpecialCharacters();
		this.value = calculateValue();
	}
	public ScrabbleWord(String wordstr, int value) {
		super();
		this.wordstr = wordstr;
		adjustSpecialCharacters();
		this.value = value;
	}
	public ScrabbleWord(String wordstr, String word, int value) {
		super();
		this.wordstr = wordstr;
		this.word = word;
		this.value = value;
	}
	public String getWordstr() {
		return wordstr;
	}
	public void setWordstr(String wordstr) {
		this.wordstr = wordstr;
		adjustSpecialCharacters();
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int calculateValue(){
		int points = 0;
		for (int i = 0; i < this.word.length(); i++)
			switch (this.word.charAt(i)) {
			case 'e':
			case 'a':
			case 'i':
			case 'n':
			case 'o':
			case 'r':
			case 's':
			case 't':
			case 'u':
			case 'l':
				points += 1;
				break;
			case 'd':
			case 'm':
			case 'g':
				points += 2;
				break;
			case 'b':
			case 'c':
			case 'p':
				points += 3;
				break;
			case 'f':
			case 'h':
			case 'v':
				points += 4;
				break;
			case 'j':
			case 'q':
				points += 8;
				break;
			case 'k':
			case 'w':
			case 'x':
			case 'y':
			case 'z':
				points += 10;
				break;
			}
		return points;
	}
	public void adjustSpecialCharacters(){
		this.word = this.wordstr;
		this.word = this.word.replace('é', 'e');
		this.word = this.word.replace('è', 'e');
		this.word = this.word.replace('ë', 'e');
		this.word = this.word.replace('ê', 'e');
		this.word = this.word.replace('à', 'a');
		this.word = this.word.replace('î', 'i');
		this.word = this.word.replace('ï', 'i');
		this.word = this.word.replace('ô', 'o');
		this.word = this.word.replace('ö', 'o');
		this.word = this.word.replace('ù', 'u');
		this.word = this.word.replace('ç', 'c');
	}
	
	@Override
	public int compareTo(ScrabbleWord o) {
		if(this.value > o.value)
			return -1;
		else if(this.value == o.value)
			return 0;
		else
			return 1;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScrabbleWord other = (ScrabbleWord) obj;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}
}
