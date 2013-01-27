//
//  PlaylistSongCell.h
//  SoundIT
//
//  Created by Samuel Chan on 2013-01-20.
//  Copyright (c) 2013 SoundIT. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PlaylistSongCell : UITableViewCell
@property (strong, nonatomic) IBOutlet UIImageView *songAlbumImage;
@property (strong, nonatomic) IBOutlet UITextView *songDescription;
@property (strong, nonatomic) IBOutlet UIImageView *voteUpButton;
@property (strong, nonatomic) IBOutlet UILabel *numVotesLabel;

@end
