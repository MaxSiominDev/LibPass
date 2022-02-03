# LibPass &middot; ![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)
Saves your library pass so that you can throw out your plastic library card
## Installation

* Download [apk](https://maxsiomin.dev/libpass)
* Clone the repo using git:
```bash
git clone https://github.com/MaxSiominDev/LibPass.git
```
## Requirements

Any Android 8.0+ device where Google Services are available

## For developers
This app uses navigation component to provide navigation. 
App receives updates from my server using retrofit. 
For generating barcodes codes I used zxing embedded.
This app saves libpasses with the Room database.
For logging I used Timber.
Analytics are provided by Firebase.
DI implemented with Hilt.

## License 
LibPass app is [MIT licensed](./LICENSE).
