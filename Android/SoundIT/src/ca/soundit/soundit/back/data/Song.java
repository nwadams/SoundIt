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
	
	private int mState;

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
	
}
