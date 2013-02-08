//
//  AddSongCell.m
//  SoundIT Canada
//
//  Created by Samuel Chan on 2013-02-07.
//  Copyright (c) 2013 SoundIT. All rights reserved.
//

#import "AddSongCell.h"

@implementation AddSongCell

@synthesize songName = _songName;
@synthesize artistName = _artistName;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
