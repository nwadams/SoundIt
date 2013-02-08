//
//  PlaylistSongCell.h
//  SoundIT
//
//  Created by Samuel Chan on 2013-01-20.
//  Copyright (c) 2013 SoundIT. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "API.h"
#import "UIAlertView+error.h"
#import <QuartzCore/QuartzCore.h>

@interface PlaylistSongCell : UITableViewCell

@property (strong, nonatomic) IBOutlet UIButton *upVoteButton;
@property (strong, nonatomic) IBOutlet UILabel *votesLabel;
@property (strong, nonatomic) IBOutlet UILabel *songNameLabel;
@property (strong, nonatomic) IBOutlet UILabel *artistNameLabel;
@property (strong, nonatomic) NSMutableString *music_track_id;
@property (strong, nonatomic) IBOutlet UIView *frontFaderView;

@end
