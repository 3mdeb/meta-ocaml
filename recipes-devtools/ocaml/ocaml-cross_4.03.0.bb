DESCRIPTION = "Objective Caml Compiler"
SECTION = "devel"
LICENSE = "QPL"

inherit cross

SRC_URI = " \
    https://github.com/ocaml/ocaml/archive/${PV}.tar.gz \
    file://0001-add-sysroot-configure-option.patch \
    file://0010-add-arm32-cross-target.patch \
    file://ocaml-redirect \
    "

LIC_FILES_CHKSUM = "file://LICENSE;md5=1d53f1a1639ae7a362cf05c3a6c466c2"

SRC_URI[md5sum] = "4ddf4977de7708f11adad692c63e87ec"
SRC_URI[sha256sum] = "fef80a338099bffc56e4d1ef35146336195a4a9e2086e8cd186c402805503a0a"

PROVIDES = "${TARGET_PREFIX}ocaml-cross"
PN = "ocaml-cross-${TARGET_ARCH}"

S = "${WORKDIR}/ocaml-${PV}"

DEPENDS += " \
    coreutils-native \
    ocaml-native \
    virtual/${TARGET_PREFIX}gcc \
    virtual/${TARGET_PREFIX}compilerlibs \
    "

SHARED_D = "${TMPDIR}/work-shared/ocaml/ocaml-${PV}-${PR}/${TARGET_SYS}"

do_configure () {
    cd ${S}
    ./configure  \
                -bindir ${SHARED_D}/usr/bin \
                -target-bindir "/usr/bin" \
                -libdir ${SHARED_D}/usr/lib \
                -mandir ${SHARED_D}/usr/share/man \
                -sysroot ${STAGING_DIR_TARGET} \
                -host ${BUILD_SYS} \
                -target ${TARGET_SYS} \
                -no-graph
}

# we can't build the optional 'bootstrap' and 'opt.opt' targets when cross-compiling,
# these require executing (cross-)compiled code (ocamlrun) on the host machine
do_compile() {
	oe_runmake world
    oe_runmake opt
#   oe_runmake opt.opt
}

do_install() {
    # run regular install
    oe_runmake install

    # create regular sysroot directory which contains links to actual OCaml
    # binaries in work-shared
    rm -rf ${D}${bindir}
    install -d  ${D}${bindir}

    # ideally we would want direct symlinks to the work-shared binaries, however
    # Yocto does not allow absolute paths. So need to patch the path into a
    # shell script and symlink to the shell script instead.
    sed -i "s|^WORK_SHARED_PATH.*|WORK_SHARED_PATH=${SHARED_D}/usr/bin|" ${WORKDIR}/ocaml-redirect
    install -m 755 ${WORKDIR}/ocaml-redirect ${D}${bindir}
    cd ${D}${bindir}
    for i in `ls ${SHARED_D}/usr/bin`; do
        # ocamlrun and ocamlyacc are the exception, they are relocatable
        if [ "$i" = "ocamlrun" ] || [ "$i" = "ocamlyacc" ]; then
            cp ${SHARED_D}/usr/bin/$i .
        else
            ln -s ocaml-redirect $i
        fi
    done
}

INHIBIT_SYSROOT_STRIP = "1"
