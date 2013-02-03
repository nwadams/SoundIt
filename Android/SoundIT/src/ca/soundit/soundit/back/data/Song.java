package ca.soundit.soundit.back.data;

public class Song {

	private String mName;
	private String mArtist;
	private String mAlbum;
	private String mCategory;
	
	private String mAlbumURL;
	private String mTrackURL;
	
	private int mCurrentRanking;
	private int mVotes;
	private boolean mVotedOn;
	
	private int mState;
	private int mMusicTrackID;

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getArtist() {
		return mArtist;
	}

	public void setArtist(String mArtist) {
		this.mArtist = mArtist;
	}

	public String getAlbum() {
		return mAlbum;
	}

	public void setAlbum(String mAlbum) {
		this.mAlbum = mAlbum;
	}

	public String getCategory() {
		return mCategory;
	}

	public void setCategory(String mCategory) {
		this.mCategory = mCategory;
	}

	public String getTrackURL() {
		return mTrackURL;
	}

	public void setAlbumURL(String mTrackURL) {
		this.mTrackURL = mTrackURL;
	}
	
	public String getAlbumURL() {
		return mAlbumURL;
	}

	public void setTrackURL(String mAlbumURL) {
		this.mAlbumURL = mAlbumURL;
	}

	public int getCurrentRanking() {
		return mCurrentRanking;
	}

	public void setCurrentRanking(int mCurrentRanking) {
		this.mCurrentRanking = mCurrentRanking;
	}

	public int getVotes() {
		return mVotes;
	}

	public void setVotes(int mVotes) {
		this.mVotes = mVotes;
	}

	public int getState() {
		return mState;
	}

	public void setState(int mState) {
		this.mState = mState;
	}

	public int getMusicTrackID() {
		return mMusicTrackID;
	}

	public void setMusicTrackID(int mMusicTrackID) {
		this.mMusicTrackID = mMusicTrackID;
	}

	public boolean isVotedOn() {
		return mVotedOn;
	}

	public void setVotedOn(boolean mVotedOn) {
		this.mVotedOn = mVotedOn;
	}
	
}
