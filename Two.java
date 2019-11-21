public class State {

    enum GameState {
        LOGIN,
        PLAYING,
        WIN,
        LOSE
    }

    String word;
    boolean[] guessedPositions;
    int remainingAtempts;
    int score;
    GameState state;

    public String getWord(){
        return word;
    }

    public boolean[] getGuessed(){
        return this.guessedPositions;
    }
    
    public int getRemainingAttemps(){
        return this.remainingAtempts;
    }
    
    public int getScore(){
        return this.score;
    }
    
    public boolean isPlaying(){
        return state == GameState.PLAYING;
    }

    public boolean isWin(){
        return state == GameState.WIN;
    }

    public boolean isLose(){
        return state == GameState.LOSE;
    }

    public void startGame(String word) {
        this.word = word;
        int len = this.word.length();
        this.guessedPositions = new boolean[len];
        this.remainingAtempts = len;
        this.state = GameState.PLAYING;
    }

    /* single char guess */
    public void guess(char x){
        String wordLower = word.toLowerCase();
        for (int i = 0; i < wordLower.length(); i++) {
            this.guessedPositions[i] = this.guessedPositions[i] || x == wordLower.charAt(i);
        }

        this.remainingAtempts = this.remainingAtempts - 1;

        // only for win check when the whole word is guessed correctly
        boolean win = true;

        for (int i = 0; i < guessedPositions.length; i++) {
            win = win && guessedPositions[i];
        }
        
        if(win){
            this.score = this.score + 1;
            this.state = GameState.WIN;
        }else if(this.remainingAtempts <= 0){
            this.score = score - 1;
            this.state = GameState.LOSE;     //if remaining attempts is smaller than 0 , lose 
        }
    }

    /*whole word guess*/
    public void guess(String x){

        this.remainingAtempts = this.remainingAtempts - 1;

        boolean win = x.toLowerCase().equals(this.word.toLowerCase());
        
        if(win){
            this.score = score + 1;
            this.state = GameState.WIN;

            for (int i = 0; i < guessedPositions.length; i++) {
                guessedPositions[i] = true;
            }

        }else if(this.remainingAtempts <= 0){
            this.score = score - 1;
            this.state = GameState.LOSE;
        }
    }


}
