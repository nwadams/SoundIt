//
//  PlaylistSongCell.m
//  SoundIT
//
//  Created by Samuel Chan on 2013-01-20.
//  Copyright (c) 2013 SoundIT. All rights reserved.
//

#import "PlaylistSongCell.h"

@implementation PlaylistSongCell

//@synthesize songAlbumImage = _songAlbumImage;
@synthesize songDescription = _songDescription;
//@synthesize voteUpButton = _voteUpButton;
@synthesize numVotesLabel = _numVotesLabel;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = [UIColor blackColor];
        
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)didPressUpVoteButton:(id)sender {
}
@end
