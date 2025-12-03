SUMMARY = "iceoryx classic platform abstraction layer"
DESCRIPTION = "This package contains the iceoryx classic platform abstraction layer in a minimal version, intended to be used with the iceoryx2 C++ bindings."
HOMEPAGE = "https://iceoryx.io"
BUGTRACKER = "https://github.com/eclipse-iceoryx/iceoryx/issues"
LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = "file://LICENSE-APACHE;md5=46d6aa0ba1fa2ed247cd8d42f20b72f4 \
                    file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d"

inherit cmake

DEPENDS = ""

SRC_URI = "git://git@github.com/eclipse-iceoryx/iceoryx.git;protocol=https;branch=main"
SRCREV = "ffd361023196d1422f1ff96723d1c83d9d4838ae"

S = "${WORKDIR}/git/iceoryx_platform"

EXTRA_OECMAKE = "-DIOX_PLATFORM_MINIMAL_POSIX=ON"

BBCLASSEXTEND = "native nativesdk"
