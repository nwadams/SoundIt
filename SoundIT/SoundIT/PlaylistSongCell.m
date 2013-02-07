//
//  PlaylistSongCell.m
//  SoundIT
//
//  Created by Samuel Chan on 2013-01-20.
//  Copyright (c) 2013 SoundIT. All rights reserved.
//

#import "PlaylistSongCell.h"

@implementation PlaylistSongCell

@synthesize songNameLabel = _songNameLabel;
@synthesize artistNameLabel = _artistNameLabel;
@synthesize votesLabel = _votesLabel;
@synthesize music_track_id = _music_track_id;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        //do initialization here

    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
