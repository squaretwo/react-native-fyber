## react-native-fyber

A react-native module for Fyber (react-native v0.25.1).

The offer wall has an imperative API.

The banner and interstitial ad formats are WIP.

Fyber iOS SDK v8.3.1 is already packaged up to save time and pain! 

### Installation

#### With [`rnpm`](https://github.com/rnpm/rnpm) (recommended)

1. `npm i react-native-fyber -S`
2. `rnpm link`

#### Manual Installation

##### iOS

1. `npm i react-native-fyber -S`

### Usage

```javascript
import { FyberOfferWall } from 'react-native-fyber'

// Display the offerwall 
FyberOfferWall.showOfferWall(userId)
```

For a full example reference to the [example project](Example).

##### Android (WIP)


### TODO
- [ ] Offer Wall (Android)
- [ ] Banner Ads (iOS/Android) 
- [ ] Interstitials (iOS/Android)
- [ ] Fully document everything in the README
