DESCRIPTION = "Bindings to the Perl Compatibility Regular Expressions library"
SECTION = "devel"
LICENSE = "LGPLv2.1"

SRC_URI_append = " \
    file://0001-add-pkg-config-option.patch \
    file://0002-add-patch-to-opam.patch \
    "

# NOTE:
#   This package depends on libpcre and we define an OE dependency below. However, the
#   opam installer is unable to detect an OE build environment and will check for the
#   presence of libpcre on the host system instead.
#   Therefore it is required to install libpcre3-dev (or equivalent) on your host
#   system.
#   Here are exact requirements from repo/default/packages/conf-libpcre/conf-libpcre.1/opam:
#
#   depexts: [
#   ["libpcre3-dev"] {os-distribution = "debian"}
#   ["libpcre-devel"] {os-distribution = "mageia"}
#   ["libpcre3-dev"] {os-distribution = "ubuntu"}
#   ["pcre-devel"] {os-distribution = "centos"}
#   ["pcre-devel"] {os-distribution = "fedora"}
#   ["pcre-devel"] {os-distribution = "rhel"}
#   ["pcre-dev"] {os-distribution = "alpine"}
#   ["pcre-devel"] {os-distribution = "opensuse"}
#   ["pcre"] {os = "freebsd"}
#   ["pcre"] {os = "macos" & os-distribution = "homebrew"}
# ]

DEPENDS += " opam-ocamlbuild pkgconfig-native libpcre"

# not sure if we actually need this...
RDEPENDS_opam-pcre += " libpcre"

do_patch () {
    # copy patch to repo'
    install -d -m 755 ${OPAM_ROOT}/repo/default/packages/pcre/pcre.${PV}/files
    install -m 644 ${WORKDIR}/0001-add-pkg-config-option.patch \
        ${OPAM_ROOT}/repo/default/packages/pcre/pcre.${PV}/files

    # ignore patch errors that might occur because we may attempt to patch the
    # same file twice as this recipe runs multiple times
    cd ${OPAM_ROOT}/repo/default/packages/pcre/pcre.${PV}
    patch -p1 -f < ${WORKDIR}/0002-add-patch-to-opam.patch || true

    # after patching the repo, we need to remove the state cache - it will get
    # rebuild the next time opam runs
    rm ${OPAM_ROOT}/repo/state.cache

    # link pcre-config to pkg-config ()
    cd ${STAGING_BINDIR_NATIVE}
    rm -f pcre-config
    ln -s pkg-config pcre-config
}



inherit opam_install

do_install_prepend () {
    export PKG_CONFIG_PATH="${PKG_CONFIG_PATH}:${STAGING_DIR_HOST}/usr/lib/pkgconfig"
}

OPAM_VERBOSE = "1"
